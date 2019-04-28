import random
import time
import carla
from properties.carla_properties import CarlaProperties

class CarlaSpawner:

  def __init__(self):
    # Carla server properties
    self.properties = CarlaProperties()
    # Array of car blueprints
    self.blueprints = []
    # Array of spawned cars
    self.actor_list = []

  def start(self):
    client = carla.Client(self.properties.host, self.properties.port)
    client.set_timeout(2.0)
    try:
      self.world = client.get_world()
      
      # Filter only 4-wheel drive vehicles
      blueprints = self.world.get_blueprint_library().filter('vehicle.*')
      blueprints = [x for x in blueprints if int(x.get_attribute('number_of_wheels')) == 4]
      self.blueprints = [x for x in blueprints if not x.id.endswith('isetta')]

      # Get available spawn points in world and shuffle them
      spawn_points = list(self.world.get_map().get_spawn_points())
      random.shuffle(spawn_points)
      print('found %d spawn points.' % len(spawn_points))
      for spawn_point in spawn_points:
        if self.try_spawn_random_vehicle_at(spawn_point):
          self.properties.number_of_vehicles -= 1
        if self.properties.number_of_vehicles <= 0:
          break
      while self.properties.number_of_vehicles > 0:
        time.sleep(self.properties.spawn_delay)
        if self.try_spawn_random_vehicle_at(random.choice(spawn_points)):
          self.properties.number_of_vehicles -= 1
      print('spawned %d vehicles, press Ctrl+C to exit.' % self.properties.number_of_vehicles)
      while True:
        time.sleep(10)

    finally:
      print('\ndestroying %d actors' % len(self.actor_list))
      client.apply_batch([carla.command.DestroyActor(x.id) for x in self.actor_list])

  def try_spawn_random_vehicle_at(self, transform):
    blueprint = random.choice(self.blueprints)
    if blueprint.has_attribute('color'):
      color = random.choice(blueprint.get_attribute('color').recommended_values)
      blueprint.set_attribute('color', color)
    blueprint.set_attribute('role_name', 'autopilot')
    vehicle = self.world.try_spawn_actor(blueprint, transform)
    if vehicle is not None:
      self.actor_list.append(vehicle)
      vehicle.set_autopilot()
      print('spawned %r at %s' % (vehicle.type_id, transform.location))
      return True
    return False


if __name__ == '__main__':
  try:
    spawner = CarlaSpawner()
    spawner.start()
  except KeyboardInterrupt:
    pass
  finally:
    print('\ndone.')
