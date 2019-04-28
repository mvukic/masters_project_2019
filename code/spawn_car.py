import random
import time
import carla
from properties.carla_properties import CarlaProperties

class CarlaSpawner:

  def __init__(self):
    # Carla world reference
    self.worls = None
    # Carla server properties
    self.properties = CarlaProperties()
    # Our vehicle
    self.vehicle = None

  def start(self):
    client = carla.Client(self.properties.host, self.properties.port)
    client.set_timeout(2.0)
    try:
      self.world = client.get_world()
      blueprint = self.get_blueprint()
      spawn_point = self.get_spawn_point()

      self.spawn_blueprint(blueprint, spawn_point)
      while True:
          time.sleep(10)
    finally:
        client.apply_batch([carla.command.DestroyActor(self.vehicle.id)])

  def spawn_blueprint(self, blueprint, transform):
    # Set color
    blueprint.set_attribute('color', '0,128,0')
    blueprint.set_attribute('role_name', 'autopilot')
    # Spawn vehicle
    vehicle = self.world.try_spawn_actor(blueprint, transform)
    if vehicle is not None:
      vehicle.set_autopilot()
      # Save vehicle reference
      self.vehicle = vehicle
      print(f'Spawned {vehicle.type_id} at {transform.location}')

  def get_blueprint(self):
    # Filter only 4-wheel drive vehicles and get one
    blueprints = self.world.get_blueprint_library().filter('vehicle.*')
    blueprints = [x for x in blueprints if int(x.get_attribute('number_of_wheels')) == 4]
    blueprints = [x for x in blueprints if not x.id.endswith('isetta')]
    return blueprints[0]

  def get_spawn_point(self):
    # Get available spawn points in world and get one
    spawn_points = list(self.world.get_map().get_spawn_points())
    return spawn_points[0]

if __name__ == '__main__':
  try:
    spawner = CarlaSpawner()
    spawner.start()
  except KeyboardInterrupt:
    pass
  finally:
    print('\ndone.')
