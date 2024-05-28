import seaborn as sns
import matplotlib.pyplot as plt
import pandas as pd
import os
import re

pattern = r'#\s*seed\s*=\s*([0-9]*\.?[0-9]+),\s*frequency\s*=\s*([0-9]*\.?[0-9]+)'


# Function to find and extract numbers

def extract_numbers_from_line(line):
    match = re.search(pattern, line)
    if match:
        return {
            'seed': float(match.group(1)),
            'frequency': float(match.group(2)),
        }
    return None


def extract_from_file(file):
    result = {}
    for line in file:
        result = extract_numbers_from_line(line)
        if result:
            break
    return result


# Seaborn
sns.set_theme()

# Load the CSV file
directory = "output_results"
# List to store individual DataFrames
dataframes = []

# Iterate over all files in the directory
for filename in os.listdir(directory):
    if filename.endswith('.csv'):
        # Create the full file path
        file_path = os.path.join(directory, filename)
        #extract the seed, frequency, averageResponseCreationTime, and jitter from the file
        with open(file_path, 'r') as file:
            other = extract_from_file(file)
        # Read the CSV file into a DataFrame
        col_names = ['events', 'lu', 'up']
        df = pd.read_csv(file_path, sep=" ", names=col_names, comment='#')
        df['lu'] = df['lu'] / df['events']
        df['up'] = df['up'] / df['events']
        for col_name, value in other.items():
            df[col_name] = value
        # Append the DataFrame to the list
        dataframes.append(df)

# Concatenate all DataFrames into a single DataFrame
data = pd.concat(dataframes, ignore_index=True)
print(data)

plt.rc('text.latex', preamble=r'\usepackage{amsmath,amssymb,amsfonts,amssymb,graphicx}')
plt.rcParams.update({"text.usetex": True})

lu_plot = sns.lineplot(data=data, x="frequency", y="lu", color="#B8DE29")
ax2 = lu_plot.twinx()
up_plot = sns.lineplot(data=data, x="frequency", y="up", color="#482677", ax=ax2)

lu_plot.set_ylabel('Lost Updates')
up_plot.set_ylabel('Useless Polling')

# LEGEND
custom_lines = [

    plt.Line2D([0], [0], color="#B8DE29", lw=3),
    plt.Line2D([0], [0], color="#482677", lw=3),
]

ax2.legend(custom_lines, ["Lost Updates", "Useless Polling"], loc="upper left")

plt.title('Title', fontsize=12)
plt.savefig("luup.pdf")

#plt.clf()

plt.show()
