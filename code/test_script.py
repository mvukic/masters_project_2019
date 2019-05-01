import random
import time
import carla
from properties.carla_properties import CarlaProperties
from utils.utils import get_lidar_sensor, get_spawn_points, get_vehicle, get_rgb_camera, get_depth_camera, print_sensor_blueprint_data

class CarlaSpawner:
  # Carla world reference
  world = None
  # Carla server properties
  properties = CarlaProperties()
  # Vehicle reference
  vehicle = None
  camera_depth = None
  camera_rgb = None
  lidar_sensor = None
  # array of reading
  readings = []

  def start(self):
    client = carla.Client(self.properties.host, self.properties.port)
    client.set_timeout(2.0)
    try:
      self.world = client.get_world()
      self.setup_vehicle()
      # self.setup_depth_camera()
      # self.setup_rbg_camera()
      time.sleep(5)
      self.setup_sensor()

      print("Simulation in progress...")
      # Main loop
      while True:
        time.sleep(10)
    finally:
      self.terminate()

  def terminate(self):
    if self.camera_depth is not None and self.camera_depth.is_alive:
      self.camera_depth.destroy()
    if self.camera_rgb is not None and self.camera_rgb.is_alive:
      self.camera_rgb.destroy()
    if self.lidar_sensor is not None and self.lidar_sensor.is_alive:
      self.lidar_sensor.destroy()
    if self.vehicle is not None and self.vehicle.is_alive:
      self.vehicle.destroy()
    print("Simulation done")

  def setup_sensor(self):
    blueprint_sensor = get_lidar_sensor(self.world.get_blueprint_library())
    # set camera time step in seconds
    blueprint_sensor.set_attribute('sensor_tick', '0.5')
    blueprint_sensor.set_attribute('channels', '360')
    blueprint_sensor.set_attribute('range', '5000')
    blueprint_sensor.set_attribute('rotation_frequency', '10')
    blueprint_sensor.set_attribute('points_per_second', '1000000')
    blueprint_sensor.set_attribute('upper_fov', '90')
    blueprint_sensor.set_attribute('lower_fov', '-80')
    # print_sensor_blueprint_data(blueprint_sensor)
    # create transform relative to vehicle
    transform = carla.Transform(carla.Location(x=3, z=3))
    self.lidar_sensor = self.world.spawn_actor(blueprint_sensor, transform, attach_to=self.vehicle)
    self.lidar_sensor.listen(lambda data: self.on_receive_data(data))
    print("Lidar sensor setup... Done")

  def setup_vehicle(self):
    spawn_point = get_spawn_points(self.world)[0]
    blueprint_vehicle = get_vehicle(self.world.get_blueprint_library())
    vehicle = self.try_spawn(blueprint_vehicle, spawn_point)
    vehicle.set_autopilot()
    self.vehicle = vehicle
    print("Vehicle setup... Done")

  def setup_rbg_camera(self):
    blueprint_camera = get_rgb_camera(self.world.get_blueprint_library())
    blueprint_camera.set_attribute('image_size_x', '800')
    blueprint_camera.set_attribute('image_size_y', '600')
    blueprint_camera.set_attribute('fov', '110')
    # set camera time step in seconds
    blueprint_camera.set_attribute('sensor_tick', '0.5')
    # create transform relative to vehicle
    transform = carla.Transform(carla.Location(x=1.5, z=2.4))
    self.camera_rgb = self.world.spawn_actor(blueprint_camera, transform, attach_to=self.vehicle)
    self.camera_rgb.listen(lambda image: image.save_to_disk('output/rgb/%06d.png' % image.frame_number))
    print("RGB camera setup... Done")

  def setup_depth_camera(self):
    blueprint_camera = get_depth_camera(self.world.get_blueprint_library())
    blueprint_camera.set_attribute('image_size_x', '800')
    blueprint_camera.set_attribute('image_size_y', '600')
    blueprint_camera.set_attribute('fov', '110')
    # set camera time step in seconds
    blueprint_camera.set_attribute('sensor_tick', '0.5')
    # create transform relative to vehicle
    transform = carla.Transform(carla.Location(x=1.5, z=2.4))
    self.camera_depth = self.world.spawn_actor(blueprint_camera, transform, attach_to=self.vehicle)
    cc = carla.ColorConverter.LogarithmicDepth
    self.camera_depth.listen(lambda image: image.save_to_disk('output/depth/%06d.png' % image.frame_number, cc))
    print("Depth camera setup... Done")

  def try_spawn(self, blueprint, transform):
    return self.world.spawn_actor(blueprint, transform)

  def save_to_files(self):
    print("Saving scans to disk...")
    for scan in self.readings[1:]:
      scan.save_to_disk('output/lidar/%06d.ply' % scan.frame_number)
    print(f"Saved {len(self.readings)-1} scans to disk")

  def on_receive_data(self, data):
    # data.save_to_disk('output/lidar/%06d.ply' % data.frame_number)
    # print(f"Points on channel 1: {data.get_point_count(1)}")
    print(f"Readings: {len(self.readings)} Lidar data: {data.frame_number}")
    self.readings.append(data)
    if len(self.readings) is 21:
      # stop reading and close
      self.terminate()
      self.save_to_files()

if __name__ == '__main__':
  try:
    saved = False
    spawner = CarlaSpawner()
    spawner.start()
  except KeyboardInterrupt:
    pass
