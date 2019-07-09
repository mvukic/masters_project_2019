import random
import time
import carla
from carla_properties import CarlaProperties
from lidar_properties import LIDARProperties
import utils as utils
from data_saver import DataSaver

class DataCollector:

  def __init__(self):
    # Spawn points
    self.spawn_points = list()
    # Carla client instance
    self.client = None
    # Carla world instance
    self.world = None
    # Observed actor reference
    self.actor = None
    # Array of npc actors
    self.npcs = list()
    # LIDAR reference
    self.lidar = None
    # array of tuples (LIDAR scan, actor transformation)
    self.scans = list()
    # Number of scans to save
    self.scan_number = 300
    # Number of npc's to spawn
    self.npc_number = 0
    # Indicates if data collection is in progress
    self.data_collection_in_progress = True
    # Debug variable to show current frame number
    self.counter = 1
    # Get every Nth scan
    self.every = 1
    # Scan counter
    self.every_counter = 0
    # start time to see if 5 seconds have passed
    self.time_start = 0
    # actor spawn point index
    self.actor_spawn_point = None

  def start(self):
    self.connect_to_carla()
    self.get_spawn_points()
    # self.spawn_npcs()
    self.spawn_actor()
    # Add some timeout for observed actor to start driving
    time.sleep(2)
    self.connect_LIDAR()
    self.loop()

  def connect_to_carla(self):
    self.client = carla.Client(CarlaProperties.host, CarlaProperties.port)
    self.client.set_timeout(2.0)
    self.world = self.client.get_world()
    settings = self.world.get_settings()
    # Set synchronous mode
    settings.synchronous_mode = True
    self.world.apply_settings(settings)

  def get_spawn_points(self):
    points = utils.get_spawn_points(self.world)
    index = random.randint(0, len(points))
    self.actor_spawn_point = points[index]
    del points[index]
    self.spawn_points = points[:]

  def spawn_npcs(self):
    print("NPCs setup...")
    if self.npc_number > len(self.spawn_points) - 1:
      self.npc_number = len(self.spawn_points) - 1
      print(f"\tRequested number of npcs to big, will spawn {self.npc_number} npcs")
    blueprints = utils.get_vehicle_blueprints(self.world)
    points = self.spawn_points[0:self.npc_number+1]
    for i in range(self.npc_number):
      # Select random actor blueprint
      actor_blueprint = random.choice(blueprints)
      print(f"\t{actor_blueprint}")
      # Get spawn point
      spawn_point = points[i]
      # Spawn the actor on selected spawn point
      spawned_actor = self.world.try_spawn_actor(actor_blueprint, self.spawn_points[i])
      # Set actor to drive itself
      spawned_actor.set_autopilot()
      # Save spawned actor reference
      self.npcs.append(spawned_actor)
    print("NPCs setup done\n")
    self.tick()

  def spawn_actor(self):
    print("Actor setup...")
    actor_blueprint = utils.get_vehicle_blueprint(self.world.get_blueprint_library())
    print(f"\t{actor_blueprint}")
    self.actor = self.world.spawn_actor(actor_blueprint, self.spawn_points[35])
    self.actor.set_autopilot()
    print("Actor setup done\n")
    self.tick()

  def connect_LIDAR(self):
    print("LIDAR setup...")
    lidar_blueprint = utils.get_lidar_sensor_blueprint(self.world.get_blueprint_library())
    print(f"\t{lidar_blueprint}")
    # set camera time step in seconds
    lidar_blueprint.set_attribute('sensor_tick', LIDARProperties.sensor_tick)
    lidar_blueprint.set_attribute('channels', LIDARProperties.channels)
    lidar_blueprint.set_attribute('range', LIDARProperties.laser_range)
    lidar_blueprint.set_attribute('rotation_frequency', LIDARProperties.rotation_frequency)
    lidar_blueprint.set_attribute('points_per_second', LIDARProperties.points_per_second)
    lidar_blueprint.set_attribute('upper_fov', LIDARProperties.upper_fov)
    lidar_blueprint.set_attribute('lower_fov', LIDARProperties.lower_fov)
    utils.print_sensor_blueprint_data(lidar_blueprint)
    # LIDAR position relative to observed actor
    self.lidar = self.world.try_spawn_actor(lidar_blueprint, LIDARProperties.lidar_relative_postion, attach_to=self.actor)
    self.lidar.listen(lambda data: self.lidar_callback(data))
    print("LIDAR setup done\n")
    self.tick()
    self.time_start = time.time()

  def lidar_callback(self, scan):
    # Start after 5 seconds
    if time.time() - self.time_start > 5:
      self.every_counter += 1
      # get every n'th scan
      if self.every_counter is self.every:
        self.scans.append((scan, self.actor.get_transform()))
        print(f"\t{self.actor.get_transform()}")
        print(f"\tCounter: {self.counter} Received frame: {scan.frame_number}")
        self.counter += 1
        self.every_counter = 0
        if self.reading_should_stop():
          self.destroy()
          self.collect_data_to_disk()
    self.tick()

  def reading_should_stop(self):
    # For n scans the number should be n+1 because first scan is unusable
    print(f"Scans: {len(self.scans)}/{self.scan_number}")
    return len(self.scans) >= self.scan_number + 5
  
  def collect_data_to_disk(self):
    print("Collecting data...")
    DataSaver().save(self.scans)
    self.data_collection_in_progress = False
    print("Data collected\n")

  def destroy(self):
    settings = self.world.get_settings()
    settings.synchronous_mode = False
    self.world.apply_settings(settings)
    print("Destroying objects...")
    if self.lidar is not None:
      print("\tDestroying LIDAR...")
      self.lidar.destroy()
    if self.actor is not None:
      print("\tDestroying actor...")
      self.actor.destroy()
    print("\tDestroying npcs...")
    for npc in self.npcs:
      npc.destroy()
    print("Destroying objects done\n")
  
  def loop(self):
    print("Waiting for data collection to end...\n")
    while self.data_collection_in_progress:
      time.sleep(1)

  def stop(self):
    self.data_collection_in_progress = False
    self.destroy()

  def tick(self):
    self.world.tick()

if __name__ == '__main__':
  dataCollector = DataCollector()
  try:
    dataCollector.start()
  except KeyboardInterrupt:
    dataCollector.stop()
    pass
