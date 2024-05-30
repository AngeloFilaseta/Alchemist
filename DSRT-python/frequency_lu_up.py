import seaborn as sns
import matplotlib.pyplot as plt
import pandas as pd
import os
import re
import matplotlib.ticker as ticker

pattern = (r'#\s*seed\s*=\s*([0-9]*\.?[0-9]+),\s*frequency\s*=\s*([0-9]*\.?[0-9]+),\s*artificialSlowDown\s*=\s*(['
           r'0-9]*\.?[0-9]+)')


# Function to find and extract values from the variables
def extract_numbers_from_line(line):
    match = re.search(pattern, line)
    if match:
        return {
            'seed': float(match.group(1)),
            'frequency': float(match.group(2)),
            'artificialSlowdown': float(match.group(3)),
        }
    return None


def extract_from_file(file):
    result = {}
    for line in file:
        result = extract_numbers_from_line(line)
        if result:
            break
    return result

# Define the custom formatter function
def custom_formatter(x, pos):
    # If the number has a fractional part, format it with one decimal place
    if x % 1:
        return f'{x:.2f}%'
    # Otherwise, format it as an integer
    else:
        return f'{int(x)}%'

# Seaborn
sns.set_theme()

# Load the CSV file
directory = "luupResults"
# List to store individual DataFrames
dataframes = []

# Iterate over all files in the directory
for filename in os.listdir(directory):
    if filename.endswith('.csv'):
        # Create the full file path
        file_path = os.path.join(directory, filename)
        with open(file_path, 'r') as file:
            other = extract_from_file(file)
        # Read the CSV file into a DataFrame
        col_names = ['time', 'events', 'observations', 'lu', 'up']
        df = pd.read_csv(file_path, sep=" ", names=col_names, comment='#')
        df = df.iloc[[-1]]
        for col_name, value in other.items():
            df[col_name] = value
        df['lu_norm'] = df['lu'] / df['events'] * 100
        df['up_norm'] = df['up'] / df['events'] * 100
        df['frequency_kHz'] = df['frequency'] / 1000
        # Append the DataFrame to the list
        dataframes.append(df)

# Concatenate all DataFrames into a single DataFrame
data = pd.concat(dataframes, ignore_index=True)

# Get unique artificialSlowdown values
unique_slowdowns = data['artificialSlowdown'].unique()
num_plots = len(unique_slowdowns)
cols = 3
rows = (num_plots // cols) + (num_plots % cols > 0)

# Order Unique Slowdowns
unique_slowdowns.sort()

# Create subplots
fig, axes = plt.subplots(rows, cols, figsize=(15, 5 * rows), sharex=True, sharey=False)

# Flatten axes array for easy iteration
axes = axes.flatten()

for ax, slowdown in zip(axes, unique_slowdowns):
    subset = data[data['artificialSlowdown'] == slowdown]
    lu_plot = sns.lineplot(data=subset, x="frequency_kHz", y="lu_norm", color="#B8DE29", ax=ax)
    ax2 = ax.twinx()
    up_plot = sns.lineplot(data=subset, x="frequency_kHz", y="up_norm", color="#482677", ax=ax2)

    ax.set_title(f'Artificial slowdown = {slowdown}ms')
    ax.set_ylabel('Lost Updates')
    ax2.set_ylabel('Useless Polling')
    ax.yaxis.set_major_formatter(ticker.FuncFormatter(custom_formatter))
    ax2.yaxis.set_major_formatter(ticker.FuncFormatter(custom_formatter))
    ax.set_xlabel('Frequency (kHz)')
    custom_lines = [
        plt.Line2D([0], [0], color="#B8DE29", lw=3),
        plt.Line2D([0], [0], color="#482677", lw=3),
    ]
    ax2.legend(custom_lines, ["Lost Updates", "Useless Polling"], loc="center right")

# Remove empty subplots
for i in range(num_plots, len(axes)):
    fig.delaxes(axes[i])

plt.tight_layout(rect=(0.0, 0.0, 1.0, 0.95))
fig.suptitle('Lost Updates and Useless Polling across different Artificial slowdowns', fontsize=16)

# Save and show
plt.savefig("luup_grid.pdf")
plt.show()
