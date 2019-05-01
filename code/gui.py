from tkinter import *
import tkinter.messagebox
import threading
from start_carla import CarlaSimulator, CarlaStatus

class Gui:

  window = None
  status_label_label = None
  start_carla_button = None
  status = "Init"

  def show(self):
    self.define_window()
    self.define_controls()
    self.window.mainloop()

  def define_window(self):
    self.window = Tk()
    self.window.title("Carla gui")
  
  def define_controls(self):
    top_frame = Frame(self.window).pack()
    bottom_frame = Frame(self.window).pack(side = "bottom")

    self.start_carla_button = Button(top_frame, text = "Start Carla", fg = "black", command=self.start_carla).pack()
    self.carla_status_label = Label(bottom_frame, text=self.status, fg = "black", bg = "white")
    self.carla_status_label.pack(fill = "x")

  def start_carla(self):
    thread = threading.Thread(target=self.start_carla_thread_function)
    thread.start()

  def start_carla_thread_function(self):
    sim = CarlaSimulator()
    sim.set_status_listener(listener=self.carla_status_listener)
    sim.start()

  def carla_status_listener(self, status):
    if status is CarlaStatus.RUNNING:
      self.status_label_label.config(text="Running")
    else:
      self.status_label_label.config(text="Stopped")


if __name__ == "__main__":
    gui = Gui()
    gui.show()