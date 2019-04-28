
All measurements are in SI units.

Python api [reference](https://github.com/carla-simulator/carla/blob/master/Docs/python_api.md)

## Spawn point

Get all available spawn point for current map.
```python
points = world.get_map().get_spawn_points()
print(points[0])
# Transform(Location(x=88.62, y=212.9, z=1.32062), Rotation(pitch=0, yaw=90, roll=0))
```

## Get blueprint

Working with blueprints.

```python
blueprint_library = world.get_blueprint_library()

print(blueprint_library.find("sensor.lidar.ray_cast"))
# ActorBlueprint(id=sensor.lidar.ray_cast,tags=[sensor, lidar, ray_cast])

print(blueprint_library.find("vehicle.nissan.patrol"))
# ActorBlueprint(id=vehicle.nissan.patrol,tags=[vehicle, nissan, patrol])

Transform(Location(x=88.62, y=212.9, z=1.32062), Rotation(pitch=0, yaw=90, roll=0))
```

Spawn actor defined with blueprint and transform.

```python
# defined location and rotation (should use world spawn points)
transform = Transform(Location(x=230, y=195, z=40), Rotation(yaw=180))
actor = world.spawn_actor(blueprint, transform)

# Destroy actor reference
actor.destroy()
```
## Actor info

Get basic info about actors location and rotation.

```python
vehicle.get_location()
# Location(x=63.8537, y=326.452, z=0.0112191)
vehicle.get_acceleration()
# Vector3D(x=-0.0189543, y=-0.00263271, z=3.71871e-06)
vehicle.get_velocity()
# Vector3D(x=-5.50429, y=0.000876591, z=-7.81883e-07)
```

## Sensors

```python
blueprint_library = world.get_blueprint_library()
sensor = blueprint_library.find("sensor.lidar.ray_cast")
```

## Recording data

Recording simulation data.
```python
client = carla.Client(...)
# start recording into file
client.start_recorder("recording01.log")
# Stop recording
client.stop_recorder()
```

```python
# Replay previous recording
client.set_replayer_time_factor(2.0)
client.replay_file("recording01.log")
```
A value greater than 1.0 will play in fast motion, and a value below 1.0 will play in slow motion.