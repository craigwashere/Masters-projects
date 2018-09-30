#include "mainwindow.h"
#include "ui_mainwindow.h"
//#include "mythread.h"

MainWindow::MainWindow(QWidget *parent) : QMainWindow(parent), ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    connect(&server, SIGNAL(newConnection()), this, SLOT(accept_connection()));

    //server.startServer();
    if (!server.listen())
    {
        QMessageBox::critical((QWidget *)this->parent(), tr("Chat Server"),
                              tr("Unable to start the server."), QMessageBox::Ok, QMessageBox::NoButton);

        server.close();
        return;
    }

    QString ipAddress;
    QList<QHostAddress> ipAddressesList = QNetworkInterface::allAddresses();
    // use the first non-localhost IPv4 address
    for (int i = 0; i < ipAddressesList.size(); ++i)
    {
        if (ipAddressesList.at(i) != QHostAddress::LocalHost && ipAddressesList.at(i).toIPv4Address())
        {
            ipAddress = ipAddressesList.at(i).toString();
            break;
        }
    }
    // if we did not find one, use IPv4 localhost
    if (ipAddress.isEmpty())
        ipAddress = QHostAddress(QHostAddress::LocalHost).toString();

    QString message = tr("IP: %1:%2").arg(ipAddress).arg(server.serverPort());
    statusBar()->showMessage(message);
}

MainWindow::~MainWindow()
{
    delete ui;
    server.close();
}

// A slot for the newConnection()
void MainWindow::accept_connection()
{
    QString IP_address;
    tcpClient temp_client;

    // nextPendingConnection() to accept the pending connection as a connected QTcpSocket.
    // This function returns a pointer to a QTcpSocket
    QTcpSocket *tcpServerConnection = server.nextPendingConnection();

    IP_address = QHostAddress(tcpServerConnection->peerAddress().toIPv4Address()).toString();
    QString message = tr("Connection from %1:%2").arg(IP_address)
                                                 .arg(tcpServerConnection->localPort());

    ui->main_text->appendPlainText(message);

    connect(tcpServerConnection, SIGNAL(readyRead()), this, SLOT(readyRead()), Qt::DirectConnection);
    connect(tcpServerConnection, SIGNAL(disconnected()), this, SLOT(disconnected()));

    temp_client.tcpServerConnection = tcpServerConnection;

    client_list.append(temp_client);
}

void MainWindow::disconnected()
{
    QTcpSocket * obj =  (QTcpSocket *)(sender());
    QByteArray Data;

    int i, j;
    for (i = 0; i < client_list.size(); i++)
        if (client_list.at(i).tcpServerConnection == obj)
            break;

    client_list.at(i).tcpServerConnection->deleteLater();

    Data = tr("%1%2").arg('\3').arg(client_list.at(i).username).toLatin1();

    QString IP_address = QHostAddress(client_list.at(i).tcpServerConnection->peerAddress().toIPv4Address()).toString();
    QString message = tr("%1 (%2:%3) disconnected") .arg(client_list.at(i).username)
                                                    .arg(IP_address).arg(client_list.at(i).tcpServerConnection->localPort());

    ui->main_text->appendPlainText(message);

    qDebug() << "Server Data:" << Data;

    for (j = 0; j < client_list.size(); j++)
        if (i != j)
            client_list.at(j).tcpServerConnection->write(Data);;

    client_list.removeAt(i);
}

void MainWindow::readyRead()
{
    int i, j, k;
    QTcpSocket * obj =  (QTcpSocket *)(sender());
    QString message;

    int client_list_size = client_list.size();

    for (i = 0; i < client_list_size; i++)
        if ((client_list.at(i).tcpServerConnection) ==  obj)
            break;

    // get the information
    QByteArray Data = client_list.at(i).tcpServerConnection->readAll();

    /*
      * If we get a new user signal then
      * 1. add username to its connection struct
      * 2. notify other users of new user
      * 3. populate new user of all current user
      *
      * not necessarily in that order
      *
      * Hopefully, a new user signal will come in its own notification
      */
    if (Data[0] == '\2')        // 2 is 'start of text' escape character, we'll use to signal a new user
    {
        client_list[i].username = Data.right(Data.size()-1);

        for (k = 0; k < client_list_size; k++)
        {
            if (k != i)
                client_list.at(k).tcpServerConnection->write(Data);
        }

        for (j = 0; j < client_list_size; j++)
        {
            if (j != i)
            {
                Data = client_list[j].username.toLatin1();
                Data.insert(0, '\2');

                client_list.at(i).tcpServerConnection->write(Data);
            }
        }
    }
    else
    {
        QString data_read = Data;
        message = tr("%1: %2").arg(client_list.at(i).username).arg(data_read);

        for (i = 0; i < client_list.size(); i++)
            client_list.at(i).tcpServerConnection->write(message.toLatin1());
    }
}
