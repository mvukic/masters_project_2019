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