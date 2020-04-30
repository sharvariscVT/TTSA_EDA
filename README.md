# TTSA_EDA
Traveling Tournament using Simulated Annealing
Overview:
Travelling Tournament Problem is a well-known problem in sports timetabling that
addresses the difficulty in creating a time-table when team travel is the main issue.
Simulated annealing is one of the approaches that use a double round-robin and
annealing algorithm to get an optimized solution for this problem.
This algorithm consists of the following features:
1. TTSA separates the tournament constraints and the pattern constraints into hard
and soft constraints and explores both feasible and infeasible schedules.
2. TTSA uses a large neighborhood of size O(n3), where n is the number of teams.
Some of the moves defining the neighborhood are rather complex and affect the
schedule in significant ways. Others can be regarded as a form of ejection
chains.
3. TTSA includes a strategic oscillation strategy to balance the time spent in the
feasible and infeasible regions.
4. TTSA incorporates the concept of “reheats” to escape from local minima with
very low temperatures.
# Functions:
To start with, a backtrack search method is used to create an initial random schedule.
Hard Constraint : Should follow a double round-robin method.
Soft Constraint : 1. No more than three consecutive home/away games.
2. A game of Ti at Tj’s home cannot be followed by a game of Tj at Ti’s
home.
There are five different functions performed on this schedule S. The neighborhood of a
schedule S is the set of the (possibly infeasible) schedules which can be obtained by
applying one of five types of moves.
1. SwapHomes: (Ti, Tj)
This function swaps home/away roles of Ti and Tj in the schedule.
2. SwapRounds: (r1, r2)
This function swaps round r1 and r2.
3. SwapTeams: (Ti, Tj):
This function swaps two teams’ schedules.
4. PartialSwapRounds:(r1,r2,t)
This function swaps round r1 and r2 for team t.
5. PartialTeamSwap: (t1,t2,r)
This function swaps values of team t1 and t2 for round r.
TTSA uses a reheating method to avoid the local minima of temperature.
The parameters defined to perform this method are maxP, maxC, maxR, temperature T,
w, beta b
