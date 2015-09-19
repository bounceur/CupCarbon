__kernel void sampleKernel(__global int *evt, __global const int *script,__global  int *iscript,__global int *iop ,__global int *step,__global char *vm, __global int *nrg) {
		
	int idx = get_global_id(0);
	int lstep = step[0];
		
	if(evt[idx]==0) {		
		evt[idx] = script[idx*lstep*2+iscript[idx]*2];
		iop[idx] = script[idx*lstep*2+iscript[idx]*2 +1];
		iscript[idx] = (iscript[idx]+1)%lstep; 		
	}        

	if(nrg[idx]<=0) {
		evt[idx]=9999999;
		vm[idx]=1;
	} 
};