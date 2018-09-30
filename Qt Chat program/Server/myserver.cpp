#include "myserver.h"
#include "mythread.h"

myServer::myServer(QObject *parent) : QTcpServer(parent)
{   }

void myServer::startServer()
{
    if (!this->listen())
    {
        QMessageBox::critical((QWidget *)this->parent(), tr("Chat Server"),
                              tr("Unable to start the server."), QMessageBox::Ok, QMessageBox::NoButton);

        QTcpServer::close();
        return;
    }


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
}

// This function is called by QTcpServer when a new connection is available.
void myServer::incomingConnection(qintptr socketDescriptor)
{
    // We have a new connection
    qDebug() << socketDescriptor << " Connecting...";

    // Every new connection will be run in a newly created thread
    myThread *thread = new myThread(socketDescriptor, this);

    // connect signal/slot
    // once a thread is not needed, it will be beleted later
    connect(thread, SIGNAL(finished()), thread, SLOT(deleteLater()));

    thread->start();
}

QString myServer::get_ip_address()
{   return ipAddress;   }
