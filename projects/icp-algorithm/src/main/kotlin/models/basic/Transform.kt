package models.basic

import models.basic.Location
import models.basic.Rotation

data class Transform(val location: Location = Location(), val rotation: Rotation = Rotation())