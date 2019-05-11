import random
import time
import carla
from carla_properties import CarlaProperties
from lidar_properties import LIDARProperties
import utils as utils

class DataCollector:
  # Carla server properties
  carla_properties = CarlaProperties()
  # LIDAR properties
  lidar_properties = LIDARProperties()
  # Spawn points
  spawn_points = []
  # Carla world reference
  world = None
  # Observed actor reference
  actor = None
  # Array of npc actors
  npcs = []
  # LIDAR reference
  lidar = None
  # array of LIDAR scans
  scans = []
  # LIDAR relative position to actor
  lidar_relative_postion = carla.Transform(carla.Location(x=0, y=0, z=4))
  # Actor transformations
  transformations = []
  # Number of scans to save
  scan_number = 2
  # Number of npc's to spawn
  npc_number = 0
  # Indicates if data collection is in progress
  data_collection_in_progress = True

  def start(self):
    self.setup()
    self.get_spawn_points()
    self.spawn_npcs()
    self.spawn_actor()
    # Add some timeout for observed actor to start driving
    time.sleep(5)
    self.connect_LIDAR()
    self.loop()

  def setup(self):
    client = carla.Client(self.carla_properties.host, self.carla_properties.port)
    client.set_timeout(2.0)
    self.world = client.get_world()

  def get_spawn_points(self):
    self.spawn_points = utils.get_spawn_points(self.world)

  def spawn_npcs(self):
    print("NPCs setup...")
    if self.npc_number > len(self.spawn_points) - 1:
      self.npc_number = len(self.spawn_points) - 1
      print(f"\tRequested number of npcs to big, will spawn {self.npc_number} npcs")
    blueprints = utils.get_vehicle_blueprints(self.world)
    points = self.spawn_points[1:self.npc_number+1]
    for i in range(self.npc_number):
      # Select random actor blueprint
      actor_blueprint = random.choice(blueprints)
      print(f"\t{actor_blueprint}")
      # Get spawn point
      actor_spwn_point = points[i]
      # Spawn the actor on selected spawn point
      spawned_actor = self.world.try_spawn_actor(actor_blueprint, actor_spwn_point)
      # Set actor to drive itself
      spawned_actor.set_autopilot()
      # Save spawned actor reference
      self.npcs.append(spawned_actor)
    print("NPCs setup done")

  def spawn_actor(self):
    print("Actor setup...")
    # Use first spawn point for observed actor
    spawn_point = self.spawn_points[0]
    actor_blueprint = utils.get_vehicle_blueprint(self.world.get_blueprint_library())
    print(f"\t{actor_blueprint}")
    self.actor = self.world.spawn_actor(actor_blueprint, spawn_point)
    self.actor.set_autopilot()
    print("Actor setup done")

  def connect_LIDAR(self):
    print("LIDAR setup...")
    lidar_blueprint = utils.get_lidar_sensor_blueprint(self.world.get_blueprint_library())
    print(f"\t{lidar_blueprint}")
    # set camera time step in seconds
    lidar_blueprint.set_attribute('sensor_tick', self.lidar_properties.sensor_tick)
    lidar_blueprint.set_attribute('channels', self.lidar_properties.channels)
    lidar_blueprint.set_attribute('range', self.lidar_properties.laser_range)
    lidar_blueprint.set_attribute('rotation_frequency', self.lidar_properties.rotation_frequency)
    lidar_blueprint.set_attribute('points_per_second', self.lidar_properties.points_per_second)
    lidar_blueprint.set_attribute('upper_fov', self.lidar_properties.upper_fov)
    lidar_blueprint.set_attribute('lower_fov', self.lidar_properties.lower_fov)
    utils.print_sensor_blueprint_data(lidar_blueprint)
    # LIDAR position relative to observed actor
    self.lidar = self.world.try_spawn_actor(lidar_blueprint, self.lidar_relative_postion, attach_to=self.actor)
    self.lidar.listen(lambda data: self.lidar_callback(data))
    print("LIDAR setup done")

  def lidar_callback(self, data):
    print(f"\tLidar data frame: {data.frame_number}")
    self.transformations.append(self.actor.get_transform())
    self.scans.append(data)
    if self.reading_should_stop():
      self.destroy()
      self.collect_data_to_disk()

  def reading_should_stop(self):
    # For n scans the number should be n+1 because first scan is unusable
    return len(self.scans) is self.scan_number + 1

  def collect_data_to_disk(self):
    print("Collecting data...")
    for index, scan in enumerate(self.scans[1:]):
      scan.save_to_disk('output/lidar/%06d.ply' % scan.frame_number)
      print(f"\t Saved scan {index+1}")
    print(f"\tSaved {self.scan_number} scan(s)")
    print("Data collected")
    self.data_collection_in_progress = False

  def destroy(self):
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
    print("Destroying objects done")
  
  def loop(self):
    print("Waiting for data collection to end...")
    while self.data_collection_in_progress:
      time.sleep(1)

if __name__ == '__main__':
  try:
    dataCollector = DataCollector()
    dataCollector.start()
  except KeyboardInterrupt:
    pass
