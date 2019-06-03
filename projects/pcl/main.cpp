#include <iostream>
#include <pcl/point_types.h>
#include<pcl/io/ply_io.h>
#include <pcl/registration/icp.h>

int main(int argc, char** argv) {
	typedef pcl::PointXYZ PointType;
	typedef pcl::PointCloud<pcl::PointXYZ> PointCloudType;
	//typedef pcl::IterativeClosestPoint<PointType, PointType, float> ICPType;

	PointCloudType::Ptr cloud_ref(new PointCloudType());
	PointCloudType::Ptr cloud_target(new PointCloudType());
	PointCloudType cloud_registered;

	pcl::io::loadPLYFile("D:\\faks\\dipl\\code\\output\\point_clouds\\000185.ply", *cloud_ref);
	std::cout << "cloud_ref size " << cloud_ref->points.size() << std::endl;
	pcl::io::loadPLYFile("D:\\faks\\dipl\\code\\output\\point_clouds\\000195.ply", *cloud_target);
	std::cout << "cloud_target size " << cloud_target->points.size() << std::endl;

	//ICPType icp;
	//icp.setInputCloud(cloud_ref);
	//icp.setInputTarget(cloud_target);
	//icp.setMaxCorrespondenceDistance(0.05);
	//icp.setMaximumIterations(50);
	//icp.setTransformationEpsilon(1e-8);
	//icp.setEuclideanFitnessEpsilon(1);
	//icp.align(cloud_registered);
	//  Eigen::Matrix4f transformation = icp.getFinalTransformation ();

	return (0);
}