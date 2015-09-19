__kernel void sampleKernel(__global const char *deadSensor, __global int *stop) {
	
	int idx = get_global_id(0);
	
	if(deadSensor[idx] == 1) 
		stop[0]=0; 
};