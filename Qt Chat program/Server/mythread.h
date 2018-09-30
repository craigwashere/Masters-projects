#ifndef MYTHREAD_H
#define MYTHREAD_H

#include <QThread>
#include <QTcpSocket>

class myThread : public QThread
{
    Q_OBJECT
    public:
        explicit myThread(qintptr ID, QObject *parent = 0);

        void run();

    signals:
        void error(QTcpSocket::SocketError socketerror);

    public slots:
        void readyRead();
        void disconnected();

    private:
        QTcpSocket *socket;
        qintptr socketDescriptor;
};

#endif // MYTHREAD_H
