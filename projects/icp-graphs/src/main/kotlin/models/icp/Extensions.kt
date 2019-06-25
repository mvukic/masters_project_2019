package models.icp

fun List<TransformMatrix>.toEuler() = this.map { it.rotation.toEuler() }