from subprocess import call
from enum import Enum

carla_version = "5"

carla_path = f"C:\CARLA\CARLA_0.9.{carla_version}\CarlaUE4.exe"
map_name = "/Game/Carla/Maps/Town01"
quality_level = "-quality-level=Low"
fps = "-benchmark -fps=15"
window = "-windowed -ResX=800 -ResY=600"
port = "-carla-port=2000"
command = f"{carla_path} {map_name} -carla-server {quality_level} {fps} {window} {port}"

class CarlaStatus(Enum):
  RUNNING = 0
  STOPPED = 1

class CarlaSimulator:

  status = CarlaStatus.STOPPED

  def start(self):
    self.status = CarlaStatus.RUNNING
    status = call(command.split())
    self.status = CarlaStatus.STOPPED

if __name__ == "__main__":
  sim = CarlaSimulator()
  sim.start()