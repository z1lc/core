import math
import random
from datetime import date

from anki.utils import int_time
from aqt import gui_hooks, mw
from aqt.qt import *

# card.queue: 0=new, -1=suspended

# what is the maximum amount of new cards you should see per day for an individual table?
MAXIMUM_PER_DAY = 15

# what is the maximum amount of days until you review all cells in a table?
# this setting will be overridden by MAXIMUM_PER_DAY
AMORTIZE_OVER = 7


def auto_manage() -> None:
    deck_ids = [d.id for d in mw.col.decks.all_names_and_ids() if "ClozeTableManager" in d.name]

    for deck_id in deck_ids:
        ctc_subdeck_note_ids = set(
            mw.col.db.list(
                """select distinct notes.id
                from notes
                join cards on notes.id = cards.nid
                where did = ?""",
                deck_id,
            )
        )

        for note_id in ctc_subdeck_note_ids:
            # get ALL cards for this note to compute target from the full set
            total = mw.col.db.scalar(
                """select count() from cards where nid = ?""",
                note_id,
            )
            if total == 0:
                continue

            # cards we can unsuspend from
            suspended_ids = mw.col.db.list(
                """select id from cards where nid = ? and queue = -1""",
                note_id,
            )

            creation_date = date.fromtimestamp(note_id / 1000)
            days_elapsed = min((date.today() - creation_date).days + 1, AMORTIZE_OVER)

            # simulate the daily schedule from creation to today to get cumulative target
            remaining = total
            for day in range(days_elapsed):
                days_left = AMORTIZE_OVER - day
                daily = min(math.ceil(remaining / days_left), MAXIMUM_PER_DAY)
                remaining -= daily
            target_unsuspended = total - remaining

            # everything not suspended counts as "unsuspended" (new, learning, review, etc.)
            current_unsuspended = total - len(suspended_ids)

            if current_unsuspended < target_unsuspended:
                to_unsuspend = random.sample(
                    suspended_ids, min(target_unsuspended - current_unsuspended, len(suspended_ids))
                )
                for card_id in to_unsuspend:
                    mw.col.db.execute(
                        """update cards set mod=?, usn=?, queue=0 where id = ?""",
                        int_time(),
                        mw.col.usn(),
                        card_id,
                    )
            elif current_unsuspended > target_unsuspended:
                # only suspend new cards (queue=0), don't yank cards out of learning/review
                new_ids = mw.col.db.list(
                    """select id from cards where nid = ? and queue = 0""",
                    note_id,
                )
                to_suspend = random.sample(new_ids, min(current_unsuspended - target_unsuspended, len(new_ids)))
                for card_id in to_suspend:
                    mw.col.db.execute(
                        """update cards set mod=?, usn=?, queue=-1 where id = ?""",
                        int_time(),
                        mw.col.usn(),
                        card_id,
                    )

    # reset main window so that due counts per subdeck update
    mw.reset()


trigger_action = QAction("ClozeTableManager: Trigger automanage", mw)
trigger_action.triggered.connect(auto_manage)
mw.form.menuTools.addAction(trigger_action)
mw.addonManager.setConfigAction(__name__, auto_manage)
gui_hooks.sync_will_start.append(auto_manage)
