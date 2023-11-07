# Out-of-the-Frying-Pan-and-Into-the-Fryer
AI Project in which I programmed and designed various bots to extinguish a spreading fire on a simulated spacecraft

The bots implemented work as folllows:
  Bot 1 - This bot plans the shortest path to the button, avoiding the initial fire cell, and then executes that
plan. The spread of the fire is ignored by the bot.
• Bot 2 - At every time step, the bot re-plans the shortest path to the button, avoiding the current fire cells,
and then executes the next step in that plan.
• Bot 3 - At every time step, the bot re-plans the shortest path to the button, avoiding the current fire cells
and any cells adjacent to current fire cells, if possible, then executes the next step in that plan. If there is no
such path, it plans the shortest path based only on current fire cells, then executes the next step in that plan.
• Bot 4 - A bot which, at every time step, attempts to predict the future fire spread and re-plans the shortest path to the button based on
said prediction if possible. If not, the bot acts in the same way bot 3 does.
