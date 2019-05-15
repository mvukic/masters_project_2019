import os

def get_lidar_sensor_blueprint(blueprint_library):
  return blueprint_library.find("sensor.lidar.ray_cast")

def get_vehicle_blueprint(blueprint_library):
  return blueprint_library.find("vehicle.nissan.patrol")

def get_spawn_points(world):
    return list(world.get_map().get_spawn_points())

def print_sensor_blueprint_data(blueprint_sensor):
  print(f"\tSensor tick: {blueprint_sensor.get_attribute('sensor_tick')}")
  print(f"\tChannels: {blueprint_sensor.get_attribute('channels')}")
  print(f"\tRange: {blueprint_sensor.get_attribute('range')}")
  print(f"\tPoints per second: {blueprint_sensor.get_attribute('points_per_second')}")
  print(f"\tRotation frequency: {blueprint_sensor.get_attribute('rotation_frequency')}")
  print(f"\tUpper FOV: {blueprint_sensor.get_attribute('upper_fov')}")
  print(f"\tLower FOV: {blueprint_sensor.get_attribute('lower_fov')}")

def get_vehicle_blueprints(world):
  blueprints = world.get_blueprint_library().filter('vehicle.*')
  blueprints = [x for x in blueprints if int(x.get_attribute('number_of_wheels')) == 4]
  return [x for x in blueprints if not x.id.endswith('isetta')]

def transform_to_string(transform, separator = " "):
  x = transform.location.x
  y = transform.location.y
  z = transform.location.z
  pitch = transform.rotation.pitch
  roll = transform.rotation.roll
  yaw = transform.rotation.yaw
  return separator.join(map(str, [x, y, z, pitch, yaw, roll]))

def create_directory(path):
  if not os.path.exists(path):
    os.makedirs(path)