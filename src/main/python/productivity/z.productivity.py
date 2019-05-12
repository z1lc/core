import os
import platform
import sys

from PyQt5.QtCore import *
from PyQt5.QtGui import *
from PyQt5.QtWebEngineWidgets import *
from PyQt5.QtWidgets import *

DEFAULT_ZOOM = 1.25
REMOVE_TOODLEDO_HEADER_JS = """
var topnav = document.getElementById('topnav-in');
topnav.parentNode.removeChild(topnav);
var sidebar = document.getElementById('ribbon');
sidebar.parentNode.removeChild(sidebar);
var list = document.getElementById('sidebar');
list.removeChild(list.getElementsByTagName('div')[0]);  //remove 'dashboard' link
"""
REMOVE_TRELLO_HEADER_JS = """
$('div.js-react-root').remove();
$('div.board-header').remove();
$('div#board').css('padding-top', '8px');
$('div.js-add-list').remove();
"""
REMOVE_WORKFLOWY_PADDING_JS = """
var style = document.createElement('style');
style.type = 'text/css';
style.innerHTML = 'div.page.active { padding: 0; margin: 18px auto; }';
document.getElementsByTagName('head')[0].appendChild(style);
"""

browsers = set()


# https://stackoverflow.com/a/47736565
class WebEnginePage(QWebEnginePage):
    def acceptNavigationRequest(self, url, _type, is_main_frame):
        if _type == QWebEnginePage.NavigationTypeLinkClicked:
            #Trello opens this automatically, seemingly to get you to sign in with Google
            if "accounts.google.com/o/oauth2/iframe" not in url.toString():
                QDesktopServices.openUrl(url)
                return False
        return True


class HtmlView(QWebEngineView):
    def __init__(self, custom_javascript, *args, **kwargs):
        QWebEngineView.__init__(self, *args, **kwargs)
        self.setPage(WebEnginePage(self))
        self.loadFinished.connect(self.on_load_finished)
        self.custom_javascript = custom_javascript

    def on_load_finished(self, ok):
        if ok:
            self.page().runJavaScript(self.custom_javascript)

    def zoom(self, delta):
        adjustment = -0.25 if delta.y() < 0 else 0.25
        for browser in browsers:
            browser.setZoomFactor(self.zoomFactor() + adjustment)

    # https://stackoverflow.com/a/7988897
    def wheelEvent(self, event):
        if event.modifiers() & Qt.ControlModifier:
            self.zoom(event.angleDelta())
        else:
            QWebEngineView.wheelEvent(self, event)


def __get_size_policy_horizontal(horizontal_stretch):
    sp = QSizePolicy(QSizePolicy.Preferred, QSizePolicy.Preferred)
    sp.setHorizontalStretch(horizontal_stretch)
    return sp


def __get_size_policy_vertical(vertical_stretch):
    sp = QSizePolicy(QSizePolicy.Preferred, QSizePolicy.Preferred)
    sp.setVerticalStretch(vertical_stretch)
    return sp


def __get_browser(url, qt_size_policy, custom_javascript, open_links_in_browser=True):
    zoom = DEFAULT_ZOOM
    if "laptop" in platform.node().lower():
        zoom = 1.75
    browser = HtmlView(custom_javascript=custom_javascript) if open_links_in_browser else QWebEngineView()
    browser.setUrl(QUrl(url))
    browser.setZoomFactor(zoom)
    browser.setSizePolicy(qt_size_policy)
    return browser


# https://www.tutorialspoint.com/pyqt/pyqt_qboxlayout_class.htm
# https://www.tutorialspoint.com/pyqt/pyqt_qsplitter_widget.htm
# noinspection PyArgumentList
def window():
    app = QApplication(sys.argv)
    win = QWidget()

    horizontal_splitter = QSplitter(Qt.Horizontal)
    browser1 = __get_browser("https://habits.toodledo.com/", __get_size_policy_horizontal(7), REMOVE_TOODLEDO_HEADER_JS)
    horizontal_splitter.addWidget(browser1)

    vertical_splitter = QSplitter(Qt.Vertical)
    vertical_splitter.setSizePolicy(__get_size_policy_horizontal(20))
    horizontal_splitter.addWidget(vertical_splitter)

    browser2 = __get_browser("https://toodledo.com/", __get_size_policy_vertical(30), REMOVE_TOODLEDO_HEADER_JS)
    browser3 = __get_browser("https://trello.com/b/Kq6NQ2LP/backlogs", __get_size_policy_vertical(20),
                             REMOVE_TRELLO_HEADER_JS)
    vertical_splitter.addWidget(browser2)
    vertical_splitter.addWidget(browser3)

    layout = QVBoxLayout()
    layout.addWidget(horizontal_splitter)
    layout.setSpacing(0)
    layout.setContentsMargins(0, 0, 0, 0)

    win.setLayout(layout)
    win.show()
    win.setWindowTitle("z.productivity")
    win.setWindowIcon(QIcon(os.path.join('checked.png')))
    win.resize(1920, 1080)

    browsers.add(browser1)
    browsers.add(browser2)
    browsers.add(browser3)

    def reload():
        for browser in browsers:
            browser.reload()

    def zoom(adjustment):
        for browser in browsers:
            browser.setZoomFactor(browser.zoomFactor() + adjustment)

    shortcut = QShortcut(QKeySequence("Ctrl+R"), horizontal_splitter)
    shortcut.activated.connect(reload)

    zoom_in = QShortcut(QKeySequence("Ctrl++"), horizontal_splitter)
    zoom_in_eq = QShortcut(QKeySequence("Ctrl+="), horizontal_splitter)
    zoom_out = QShortcut(QKeySequence("Ctrl+-"), horizontal_splitter)
    zoom_in.activated.connect(lambda: zoom(0.25))
    zoom_in_eq.activated.connect(lambda: zoom(0.25))
    zoom_out.activated.connect(lambda: zoom(-0.25))

    sys.exit(app.exec_())


if __name__ == '__main__':
    window()
