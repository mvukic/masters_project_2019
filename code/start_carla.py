import os

carla_path = "C:\CARLA\CARLA_0.9.4\CarlaUE4.exe"
map_name = "/Game/Carla/Maps/Town01"
quality_level = "-quality-level=Low"
fps = "-benchmark -fps=15"
window = "-windowed -ResX=800 -ResY=600"
port = "-carla-port=2000"

# Construct command
command = f"{carla_path} {map_name} -carla-server {quality_level} {fps} {window} {port}"

os.system(command)
