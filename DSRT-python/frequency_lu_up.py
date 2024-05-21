import pandas as pd
import seaborn as sns
import seaborn.objects as so
import numpy as np
import os
import matplotlib.pyplot as plt

# Seaborn
sns.set_theme()

# Load the CSV file
directory = "RESULTS"

data = {
    'f': np.linspace(0.0, 3.0, 50),
    'lu': np.linspace(0.5, 20.0, 50),
    'up': np.linspace(30.0, 0.0, 50)
}
plt.rc('text.latex', preamble=r'\usepackage{amsmath,amssymb,amsfonts,amssymb,graphicx}')
plt.rcParams.update({"text.usetex": True})

sns.
for filename in os.listdir(directory):



    col_names = ['Time', 'Nodes', 'Limited', 'Full']
    f = os.path.join(directory, filename)
    df = pd.read_csv(f, sep=" ", names=col_names, comment='#')
    df["Nodes"] = df["Nodes"].astype(int)
    melted_df = pd.melt(
        df,
        id_vars=['Time', 'Nodes'],
        value_vars=['Limited', 'Full'],
        var_name='Type',
        value_name='Size'
    )
    melted_df['Size (kB)'] = melted_df['Size'] / 1000
    # Assign plots
    limited_df = melted_df.query('Type == "Limited"')
    full_df = melted_df.query('Type == "Full"')
    nodes_plot = sns.lineplot(data=limited_df, x="Time", y="Nodes", color="#fde725")
    ax2 = nodes_plot.twinx()
    full_size_plot = sns.lineplot(data=full_df, x="Time", y="Size (kB)", ax=ax2, color="#440154")
    limited_size_plot = sns.lineplot(data=limited_df, x="Time", y="Size (kB)", ax=ax2, color="#21918c")

    # ✨ Aesthetic ✨

    ax2.set(ylabel='Size (kB)')
    nodes_plot.set_ylabel('Number of nodes')
    nodes_plot.set_ylim(0, df["Nodes"].max() + 10)
    # LEGEND
    custom_lines = [
        plt.Line2D([0], [0], color="#fde725", lw=3),
        plt.Line2D([0], [0], color="#21918c", lw=3),
        plt.Line2D([0], [0], color="#440154", lw=3)
    ]
    ax2.legend(custom_lines, ["Number of nodes", "Specific response size", "Full response size"], loc="best")

    plt.title('Influence of Node quantity on response size ', fontsize=12)
    plt.savefig(filename + ".pdf")
    plt.clf()
    # limited_size_plot.set_yticks(np.linspace(0, 2, num=4))
    # nodes_plot.set_yticks(np.linspace(0, 30, num=2))
