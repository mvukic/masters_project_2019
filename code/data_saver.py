import utils as utils
import time
import threading
from concurrent.futures import ThreadPoolExecutor, as_completed
from lidar_properties import LIDARProperties

class DataSaver:

  def __init__(self):
    # Point clouds directory
    self.pc_path = 'output/point_clouds'
    # Actor transforms directory
    self.trans_path = 'output/actor_transforms'
    # Relative transform path
    self.lidar_relative_transform_path = 'output/relative_transform.txt'

  def initialize_folders(self):
    utils.create_directory(self.trans_path)
    utils.create_directory(self.pc_path)

  def save(self, scans):
    self.initialize_folders()
    self.save_rel_trans()
    start_time = time.time()

    with ThreadPoolExecutor(max_workers=10) as executor:
      jobs = list()
      for index, (scan, transform) in enumerate(scans[1:]):
        job = executor.submit(self.process_pair, scan, transform, index)
        jobs.append(job)
      for future in as_completed(jobs):
        future.result()
    print(f"\tSaved {len(scans)-1} scan(s)")
    print(f"\tTime elapsed {time.time() - start_time}")

  def process_pair(self, scan, transform, index):
    scan_path = f'{self.pc_path}/{scan.frame_number:06d}.ply'
    self.save_scan(scan, scan_path, index + 1)
    trans_path =  f'{self.trans_path}/{scan.frame_number:06d}.txt'
    self.save_transform(transform, scan.timestamp, trans_path, index + 1)

  def save_rel_trans(self):
    with open(self.lidar_relative_transform_path, 'w+') as file:
      file.write(utils.transform_to_string(LIDARProperties.lidar_relative_postion))

  def save_scan(self, scan, path, index = 0):
      scan.save_to_disk(path)
      print(f"\t Saved scan {index}")

  def save_transform(self, transform, timestamp, path, index = 0):
    with open(path, 'w+') as file:
      file.write(f"{timestamp}\n{utils.transform_to_string(transform)}")
    print(f"\t Saved transform {index}")
