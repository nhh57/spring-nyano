vegeta attack -targets=D:\Java\spring-nyano\benchmark\targets.txt -name=2000qps -duration=10s -rate=100 | vegeta encode >  D:\Java\spring-nyano\benchmark\result_2000qps.json


vegeta attack -targets=/home/hoanghai/Project/Java/spring-nyano/benchmark/targets_2.txt -name=2000qps -duration=10s -rate=100 | vegeta encode >  /home/hoanghai/Project/Java/spring-nyano/benchmark/result_2000qps.json

vegeta attack -targets=./benchmark/targets_2.txt -name=2000qps -duration=10s -rate=100 | vegeta encode >  ./benchmark/result_2000qps.json