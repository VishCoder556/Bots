import random

# Define the environment for multiplication problems
class MultiplicationEnvironment:
    def __init__(self):
        self.first_number = random.randint(1, 9)  # Randomly generate the first number
        self.second_number = random.randint(1, 9)  # Randomly generate the second number
        self.target_value = self.first_number * self.second_number  # The target value for the multiplication problem
        self.current_value = random.randint(1, 100)  # The current value of the dial

    def reset(self):
        self.first_number = random.randint(1, 9)
        self.second_number = random.randint(1, 9)
        self.target_value = self.first_number * self.second_number
        self.current_value = random.randint(1, 100)

    def step(self, action):
        # Perform the chosen action (increase or decrease the dial value)
        if action == 0:  # Decrease dial value
            self.current_value -= 1
        else:  # Increase dial value
            self.current_value += 1

        # Calculate the reward based on how close the current value is to the target value
        reward = -abs(self.current_value - self.target_value)

        # Check if the episode is finished
        done = self.current_value == self.target_value

        return reward, done

# Initialize the Q-table
num_actions = 2  # Two actions: 0 for decrease, 1 for increase
num_dials = 1  # Single dial
q_table = [[0 for _ in range(num_actions)] for _ in range(num_dials)]

# Define hyperparameters
learning_rate = 0.1
discount_factor = 0.9
num_episodes = 10000

# Training loop
for episode in range(num_episodes):
    env = MultiplicationEnvironment()
    done = False
    state = 0  # In this simple case, we only have one dial, so the state is 0.

    while not done:
        # Choose an action (0 for decrease, 1 for increase) using epsilon-greedy policy
        epsilon = 0.05  # Lower exploration rate to prioritize exploitation
        if random.random() < epsilon:
            action = random.randint(0, num_actions - 1)
        else:
            action = 0 if q_table[state][0] > q_table[state][1] else 1

        # Perform the chosen action and observe the reward and new state
        reward, done = env.step(action)
        new_state = 0  # In this simple case, the new state is still 0 since we only have one dial.

        # Update the Q-table using the Q-learning equation
        current_q_value = q_table[state][action]
        max_next_q_value = max(q_table[new_state])
        updated_q_value = current_q_value + learning_rate * (reward + discount_factor * max_next_q_value - current_q_value)
        q_table[state][action] = updated_q_value

# After training, let's test the student bot's ability to solve multiplication problems
for _ in range(5):  # Let's test with 5 different multiplication problems
    env = MultiplicationEnvironment()
    state = 0
    done = False
    num_steps = 0

    while not done:
        num_steps += 1
        action = 0 if q_table[state][0] > q_table[state][1] else 1
        reward, done = env.step(action)

    print(f"{env.first_number} * {env.second_number} =", env.target_value)
    print("Student Bot's Answer:", env.current_value)
    print("Number of Steps:", num_steps)
    print()
