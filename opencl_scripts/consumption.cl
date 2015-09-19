__kernel void sampleKernel(__global  char *links,__global const int *min, __global const int *cmdType, __global int *energy,__global char *deadSensor, __global int *size, __global int *event, __global int *event2) {
	
	int lmin = min[0];
	int lsize = size[0];

	int idx = get_global_id(0);
	
	int consumption = 0;
	for(int i=0; i < lsize; i++)
		consumption = consumption + (links[idx * lsize + i] * cmdType[i] * (1 - deadSensor[i]));
	
	energy[idx] = energy[idx] - (consumption * lmin);
	
	if(energy[idx]<=0) {
		energy[idx]=0;
	}
	
	event[idx] = event[idx] - lmin;
	event2[idx] = event2[idx] - lmin;
};