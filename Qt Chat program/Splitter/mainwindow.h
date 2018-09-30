#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QtNetwork>
#include "dia_server.h"

class QAction;
class QActionGroup;

class QMenu;

namespace Ui
{
    class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private:
    dia_server *server_dialog;
    Ui::MainWindow *ui;
    void createActions();
    void createMenus();

    QMenu *fileMenu;
    QAction *ServerAct;
    QAction *aboutAct;
    QMenu *helpMenu;

    QTcpSocket *client_socket;
    QString username, server_address, port_number;

    bool ok_to_add_username(QString username);
    bool connect_to_server(QString server_address, QString port_number);
    bool eventFilter(QObject *obj, QEvent *event);
    bool send_message();

public slots:
    void readyRead();
private slots:
    void Server();
    void about();
    void displayError(QAbstractSocket::SocketError);
    void connected();
};

#endif // MAINWINDOW_H
