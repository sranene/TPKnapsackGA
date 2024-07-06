# Task Parallelism in the Genetic Algorithm for the Knapsack problem
The system architecture for the Knapsack Genetic Algorithm (GA) with Actor Model follows a parallel approach where independent actors collaborate using message-based communication, avoiding direct memory sharing. Here’s a breakdown of its operation:

1. **Initialization**: Starts with a ManagerActor and a Customer in the main process. The Customer initiates the process by sending a BootstrapMessage. It generates the initial population and sends a WorkOnPopulationMessage to the ManagerActor.

2. **Fitness Calculation (MFActors)**: The ManagerActor creates MFActors (equal to available processors) to compute fitness values for population chunks. MFActors send CalculateMessage to the ManagerActor upon completion.

3. **Fitness Response**: Each MFActor computes fitness and sends a ResponseMFMessage to the ManagerActor. Upon receiving all responses, the ManagerActor sends SystemKillMessage to terminate MFActors.

4. **Crossover/Mutation Calculation (CMActors)**: After receiving all MFActor responses, the ManagerActor identifies the best individual and spawns CMActors (equal to available processors) for crossover and mutation.

5. **Crossover/Mutation Response**: CMActors execute crossover/mutation and send ResponseCMMessage to the ManagerActor. Similar to MFActors, ManagerActor sends SystemKillMessage after receiving all CMActor responses.

6. **Customer Response**: Upon receiving all CMActor responses, the ManagerActor sends the final population to the Customer.

7. **Termination**: The Customer increments generation count until reaching the desired number. Upon completion, it sends SystemKillMessage to terminate the ManagerActor and itself.
Justifications and Synchronization:

## Justifications and Synchronization

ManagerActor supervises MFActors and CMActors for parallel fitness and genetic operations.
Optimization includes handling best individual computation within ManagerActor and message-based synchronization.
Actor independence ensures no direct memory sharing, enhancing system efficiency.

## Benchmark

* Sequential Average: 93.4313 seconds
* Parallel Average: 69.6493 seconds
* Actor Model Average: 277.8547 seconds

## Considerations

The current implementation focuses solely on mutations without incorporating crossover simultaneously. This design choice may limit the exploration of genetic diversity in the population, which could be a potential area for improvement.
Actor creation and termination may impact execution time.
Message exchange overhead affects performance.
Use of clone() mitigates memory sharing but introduces overhead.
The Knapsack problem’s limited parallelism due to task dependencies affects efficiency.

# Conclusion
In conclusion, while Actor Model benefits complex and highly parallel problems, its overhead in synchronization and message passing affects performance compared to sequential and thread-based parallelism.
