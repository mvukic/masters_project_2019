import utils as utils
import time
import threading
from concurrent.futures import ThreadPoolExecutor, as_completed
from lidar_properties import LIDARProperties

class DataSaver:

  def __init__(self):
    # Point clouds directory
    self.point_clouds_path = 'output/point_clouds'
    # Actor transforms directory
    self.actor_transforms_path = 'output/actor_transforms'
    # Relative transform path
    self.lidar_relative_transform_path = 'output/relative_transform.txt'

  def initialize_folders(self):
    utils.create_directory(self.actor_transforms_path)
    utils.create_directory(self.point_clouds_path)

  def save(self, scans):
    self.initialize_folders()
    self.save_LIDAR_to_actor_relative_transform()
    start_time = time.time()

    with ThreadPoolExecutor(max_workers=10) as executor:
      jobs = [executor.submit(self.process_pair, scan, transform, index) for index, (scan, transform) in enumerate(scans[1:])]
      for future in as_completed(jobs):
        future.result()
    print(f"\tSaved {len(scans)-1} scan(s)")
    print(f"\tTime elapsed {time.time() - start_time}")

  def process_pair(self, scan, transform, index):
    self.save_scan(scan, f'{self.point_clouds_path}/{scan.frame_number:06d}.ply', index + 1)
    self.save_transform(transform, scan.timestamp, f'{self.actor_transforms_path}/{scan.frame_number:06d}.txt', index + 1)

  def save_LIDAR_to_actor_relative_transform(self):
    with open(self.lidar_relative_transform_path, 'w+') as file:
      file.write(utils.transform_to_string(LIDARProperties.lidar_relative_postion))

  def save_scan(self, scan, path, index = 0):
      scan.save_to_disk(path)
      print(f"\t Saved scan {index}")

  def save_transform(self, transform, timestamp, path, index = 0):
    with open(path, 'w+') as file:
      file.write(f"{timestamp}\n{utils.transform_to_string(transform)}")
    print(f"\t Saved transform {index}")
