from subprocess import call
from enum import Enum

carla_path = "C:\CARLA\CARLA_0.9.4\CarlaUE4.exe"
map_name = "/Game/Carla/Maps/Town01"
quality_level = "-quality-level=Low"
fps = "-benchmark -fps=15"
window = "-windowed -ResX=800 -ResY=600"
port = "-carla-port=2000"
command = f"{carla_path} {map_name} -carla-server {quality_level} {fps} {window} {port}"

class CarlaStatus(Enum):
  RUNNING = 1
  STOPPED = 2

class CarlaSimulator:

  status = CarlaStatus.STOPPED
  listener = None

  def set_status_listener(self, listener):
    self.listener = listener

  def start(self):
    self.status = CarlaStatus.RUNNING
    self.notify()
    status = call(command.split())
    self.status = CarlaStatus.STOPPED
    self.notify()

  def notify(self):
    if self.listener is not None:
      self.listener(self.status)

def status_listener(status):
  print(f"Status: {status}")

if __name__ == "__main__":
  sim = CarlaSimulator()
  sim.set_status_listener(listener=status_listener)
  sim.start()