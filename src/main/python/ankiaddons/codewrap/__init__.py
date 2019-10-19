import os

from anki.hooks import addHook


# cross out the currently selected text
def onCodeTag(editor):
    editor.web.eval("wrap('<code>', '</code>');")


def addMyButton(buttons, editor):
    return buttons + [editor.addButton(icon=os.path.join(os.path.dirname(__file__), "code.png"),
                                       cmd="CODE",
                                       func=lambda s=editor: onCodeTag(editor),
                                       tip="Add <code> (ctrl+alt+c)",
                                       toggleable=False,
                                       label="",
                                       keys="ctrl+alt+c",
                                       disables=True)]


addHook("setupEditorButtons", addMyButton)
