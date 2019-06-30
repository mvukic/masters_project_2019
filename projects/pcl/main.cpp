#include <iostream>
#include <pcl/point_types.h>
#include<pcl/io/ply_io.h>
#include <pcl/registration/icp.h>
#include <iostream>
#include "boost/filesystem.hpp"
#include <filesystem>
#include <fstream>
#include <pcl/filters/voxel_grid.h>
#include <pcl/filters/statistical_outlier_removal.h>
#include <pcl/segmentation/extract_clusters.h>

using namespace std;
using namespace pcl;
using namespace boost::filesystem;


typedef PointXYZ PT;
typedef PointCloud<PT> PointCloudType;
typedef IterativeClosestPoint<PT, PT, double> ICP;

PointCloudType::Ptr cloud_ref(new PointCloudType());
PointCloudType::Ptr cloud_target(new PointCloudType());
PointCloudType::Ptr cloud_registered(new PointCloudType());

PointCloudType::Ptr cloud_ref_filtered(new PointCloudType());
PointCloudType::Ptr cloud_target_filtered(new PointCloudType());

string root_point_clouds = "D:\\faks\\dipl\\code\\output\\point_clouds\\";
string root_results = "D:\\faks\\dipl\\code\\output\\icp_results\\";

vector<path> get_files() {
	vector<path> paths;
	path p(root_point_clouds);
	directory_iterator end_itr;
	for (directory_iterator itr(p); itr != end_itr; ++itr)
	{
		if (is_regular_file(itr->path())) {
			paths.push_back(itr->path());
		}
	}
	return paths;
}

ICP setupICP() {
	ICP icp;
	icp.setMaxCorrespondenceDistance(0.05);
	icp.setMaximumIterations(500);
	icp.setTransformationEpsilon(1e-8);
	icp.setEuclideanFitnessEpsilon(1);
	return icp;
}

void load_point_cloud(string path, PointCloudType& cloud) {
	pcl::io::loadPLYFile(path, cloud);
}

string mat_to_string(const Eigen::Matrix4d mat) {
	std::stringstream ss;
	ss << mat;
	return ss.str();
}

void save_to_file(string filename, string data, double fitness, Eigen::Vector3d rpy) {
	string save_path = root_results + "\\" + filename;
	cout << save_path << endl;
	std::ofstream output;
	output.open(save_path) ;
	output << fitness << endl;
	output << data << endl;
	output << rpy[0] << " " << rpy[1] << " " << rpy[2];
	output.close();
}

void print_4x4_matrix(Eigen::Matrix4d matrix) {
	printf("Rotation matrix :\n");
	printf("    | %6.6f %6.6f %6.6f | \n", matrix(0, 0), matrix(0, 1), matrix(0, 2));
	printf("R = | %6.6f %6.6f %6.6f | \n", matrix(1, 0), matrix(1, 1), matrix(1, 2));
	printf("    | %6.6f %6.6f %6.6f | \n", matrix(2, 0), matrix(2, 1), matrix(2, 2));
	printf("Translation vector :\n");
	printf("t = < %6.6f, %6.6f, %6.6f >\n\n", matrix(0, 3), matrix(1, 3), matrix(2, 3));
}

Eigen::Matrix3d mat4x4_to_3x3(Eigen::Matrix4d matrix) {
	Eigen::Matrix3d mat;
	mat(0, 0) = matrix(0, 0); mat(1, 0) = matrix(1, 0); mat(2, 0) = matrix(2, 0);
	mat(0, 1) = matrix(0, 1); mat(1, 1) = matrix(1, 1); mat(2, 1) = matrix(2, 1);
	mat(0, 2) = matrix(0, 2); mat(1, 2) = matrix(1, 2); mat(2, 2) = matrix(2, 2);
	return mat;
}

void save_matrix(ICP icp, string first, string second) {
	Eigen::Matrix4d transformation = icp.getFinalTransformation();
	double fitness = icp.getFitnessScore();
	string filename = first + "-" + second + ".txt";
	Eigen::Matrix3d mat = mat4x4_to_3x3(transformation);
	Eigen::Vector3d rpy = mat.eulerAngles(0, 1, 2);
	//print_4x4_matrix(transformation);
	save_to_file(filename, mat_to_string(transformation), fitness, rpy);
}

void process_files_genralized(vector<path> paths, ICP icp) {

	for (long i = 0; i < paths.size() - 1; i++) {
		if (i == 0) {
			load_point_cloud(paths.at(i).string(), *cloud_ref);
		}
		load_point_cloud(paths.at(i + 1).string(), *cloud_target);

		string first = paths.at(i).stem().string();
		string second = paths.at(i + 1).stem().string();

		icp.setInputCloud(cloud_ref);
		icp.setInputTarget(cloud_target);
		icp.align(*cloud_registered);

		if (icp.hasConverged()) {
			save_matrix(icp, first, second);
		}

		*cloud_ref = *cloud_target;
	}
}

void downsample_using_voxel_grid(PointCloudType::Ptr& cloud, float leafsize, PointCloudType::Ptr& downsampled) {
	VoxelGrid<PT> vg;
	vg.setInputCloud(cloud);
	vg.setLeafSize(leafsize, leafsize, leafsize);
	vg.filter(*downsampled);
}

void downsample_using_statical_outliner(PointCloudType::Ptr& cloud, PointCloudType::Ptr& cloudFiltered) {
	StatisticalOutlierRemoval<PT> sor;
	sor.setInputCloud(cloud);
	sor.setMeanK(15);
	sor.setStddevMulThresh(1.0);
	sor.filter(*cloudFiltered);
}

void process_files_with_feature_detection(vector<path> paths, ICP icp) {
	for (long i = 0; i < paths.size() - 1; i++) {
		if (i == 0) {
			load_point_cloud(paths.at(i).string(), *cloud_ref);
		}
		load_point_cloud(paths.at(i + 1).string(), *cloud_target);

		string first = paths.at(i).stem().string();
		string second = paths.at(i + 1).stem().string();
		cout << first << " " << second << endl;
		//if (i == 0) {
		//	downsample_using_statical_outliner(cloud_ref, cloud_ref_filtered);
		//}
		//downsample_using_statical_outliner(cloud_target, cloud_target_filtered);
		if (i == 0) {
			downsample_using_voxel_grid(cloud_ref, 1.0f, cloud_ref_filtered);
		}
		downsample_using_voxel_grid(cloud_target, 1.0f, cloud_target_filtered);

		cout << cloud_ref->size() << " <-> " << cloud_ref_filtered->size() << endl;
		cout << cloud_target->size() << " <-> " << cloud_target_filtered->size() << endl;
		cout << endl;

		icp.setInputCloud(cloud_ref_filtered);
		icp.setInputTarget(cloud_target_filtered);
		icp.align(*cloud_registered);
		if (icp.hasConverged()) {
			save_matrix(icp, first, second);
		}

		*cloud_ref = *cloud_target;
		*cloud_ref_filtered = *cloud_target_filtered;
	}
}


int main(int argc, char** argv) {
	vector<path> paths = get_files();
	ICP icp = setupICP();
	process_files_with_feature_detection(paths, icp);
	return 0;
}
