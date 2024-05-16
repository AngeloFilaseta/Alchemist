import pandas as pd
import seaborn as sns
import seaborn.objects as so
import os

# Seaborn
sns.set_theme()

# Load the CSV file
directory = "RESULTS"
for filename in os.listdir(directory):
    col_names = ['Time', 'Nodes', 'Limited', 'Full']
    f = os.path.join(directory, filename)
    df = pd.read_csv(f, sep=" ", names=col_names)

    melted_df = pd.melt(
        df,
        id_vars=['Time', 'Nodes'],
        value_vars=['Limited', 'Full'],
        var_name='Type',
        value_name='Size'
    )
    melted_df['Size in KB'] = melted_df['Size'] / 1000

    # Display the first few rows of the dataframe to ensure it's loaded correctly
    print(melted_df.head())
    # Create the primary plot
    p = (so.Plot(melted_df, "Time", "Size in KB", color="Type")
     .add(so.Line(marker="o"), so.Agg(), linestyle=None))
    # Show the plot
    p.show()
