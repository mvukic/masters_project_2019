def get_lidar_sensor(blueprint_library):
  return blueprint_library.find("sensor.lidar.ray_cast")

def get_rgb_camera(blueprint_library):
  return blueprint_library.find('sensor.camera.rgb')

def get_depth_camera(blueprint_library):
  return blueprint_library.find('sensor.camera.depth')

def get_vehicle(blueprint_library):
  return blueprint_library.find("vehicle.nissan.patrol")

def get_spawn_points(world):
    return list(world.get_map().get_spawn_points())

def print_sensor_blueprint_data(blueprint_sensor):
  print(f"Sensor tick: {blueprint_sensor.get_attribute('sensor_tick')}")
  print(f"Channels: {blueprint_sensor.get_attribute('channels')}")
  print(f"Range: {blueprint_sensor.get_attribute('range')}")
  print(f"Points per second: {blueprint_sensor.get_attribute('points_per_second')}")
  print(f"Rotation frequency: {blueprint_sensor.get_attribute('rotation_frequency')}")
  print(f"Upper FOV: {blueprint_sensor.get_attribute('upper_fov')}")
  print(f"Lower FOV: {blueprint_sensor.get_attribute('lower_fov')}")