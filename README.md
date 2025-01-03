vegeta attack -targets=D:\Java\spring-nyano\benchmark\targets.txt -name=2000qps -duration=10s -rate=100 | vegeta encode >  D:\Java\spring-nyano\benchmark\result_2000qps.json


vegeta attack -targets=/home/hoanghai/Project/Java/spring-nyano/benchmark/targets_2.txt -name=2000qps -duration=10s -rate=100 | vegeta encode >  /home/hoanghai/Project/Java/spring-nyano/benchmark/result_2000qps.json

echo "GET http://10.56.66.52:1122/ticket/1/detail/1" | vegeta attack -name=2000qps -duration=10s -rate=100 | tee benchmark/results_2000qps.bin | vegeta report