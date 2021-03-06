import carla

# Default Carla properties
class LIDARProperties:
  # Time step between two scans in seconds
  # With bigger step we get larger set of points in cloud
  # It's connected with rotation_frequency
  sensor_tick = str(0.0)
  # Number of lasers in vertical
  channels = str(360)
  # Range of individual laser in centimeters
  laser_range = str(3000.0)
  # Lidar rotation frequency
  rotation_frequency = str(20.0)
  # Points generated by all lasers per second
  points_per_second = str(1300000)
  # Upper laser field of view in degrees
  upper_fov = str(45.0)
  # Lower laser field of view in degrees
  lower_fov = str(-60.0)

  # LIDAR relative position to actor
  lidar_relative_postion = carla.Transform(carla.Location(x=0, y=0, z=4))

