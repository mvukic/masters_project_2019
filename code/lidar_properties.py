
# Default Carla properties
class LIDARProperties:
  # Time step between two scansm in seconds
  sensor_tick = str(0.5)
  # Number of lasers
  channels = str(360)
  # Range of individual laser in centimeters
  laser_range = str(3000)
  # Lidar rotation frequency
  rotation_frequency = str(10)
  # Points generated by all lasers per second
  points_per_second = str(1_000_000)
  # Upper laser field of view
  upper_fov = str(45)
  # Lower laser field of view
  lower_fov = str(-80)
