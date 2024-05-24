import seaborn as sns
import numpy as np
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


lu = sns.lineplot(data=data, x="f", y="lu", color="#B8DE29")
ax2 = lu.twinx()
up = sns.lineplot(data=data, x="f", y="up", color="#482677")
#Aesthetics

lu.set(xlabel='Frequency')
lu.set(ylabel='Lost Updates')
ax2.set(ylabel='Useless Polling')
# LEGEND
custom_lines = [
    plt.Line2D([0], [0], color="#482677", lw=3),
    plt.Line2D([0], [0], color="#B8DE29", lw=3)
]
ax2.legend(custom_lines, ["Useless Polling", "Lost Updates"], loc="upper left")

plt.title('Lost Updates And Useless Polling changes over Frequency ', fontsize=12)
plt.savefig("uplu.pdf")
plt.clf()