import glob
import os
import sys
import argparse
import random
import time
import carla

class CarlaSpawner:

  def __init__(self):
    self.number_of_vehicles = 50
    self.spawn_delay = 2.0
    self.host = "localhost"
    self.port = 2000
    self.blueprints = []
    self.actor_list = []

  def start(self):
    client = carla.Client(self.host, self.port)
    client.set_timeout(2.0)
    try:
      self.world = client.get_world()

      blueprints = self.world.get_blueprint_library().filter('vehicle.*')
      blueprints = [x for x in blueprints if int(x.get_attribute('number_of_wheels')) == 4]
      self.blueprints = [x for x in blueprints if not x.id.endswith('isetta')]
      # @todo Needs to be converted to list to be shuffled.
      spawn_points = list(self.world.get_map().get_spawn_points())
      random.shuffle(spawn_points)
      print('found %d spawn points.' % len(spawn_points))

      for spawn_point in spawn_points:
          if self.try_spawn_random_vehicle_at(spawn_point):
              self.number_of_vehicles -= 1
          if self.number_of_vehicles <= 0:
              break
      while self.number_of_vehicles > 0:
          time.sleep(spawn_delay)
          if self.try_spawn_random_vehicle_at(random.choice(spawn_points)):
              self.number_of_vehicles -= 1
      print('spawned %d vehicles, press Ctrl+C to exit.' % self.number_of_vehicles)
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
