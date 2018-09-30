#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QTcpServer>
#include <QtWidgets>
#include <QtNetwork>


namespace Ui
{   class MainWindow;   }

QT_BEGIN_NAMESPACE
class QLabel;
class QTcpServer;
QT_END_NAMESPACE

typedef struct _tcpClient
{
    QTcpSocket *tcpServerConnection;
    QString username;
} tcpClient;

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();
 //   void startServer();

public slots:
    // server accepts a request from a client
    void accept_connection();
    void disconnected();
    void readyRead();

private:
    Ui::MainWindow *ui;

    QTcpServer server;
    QList<tcpClient> client_list;

//    QTcpSocket *tcpServerConnection;
};

#endif // MAINWINDOW_H
