#ifndef MYSERVER_H
#define MYSERVER_H



class myServer : public QTcpServer
{
    Q_OBJECT
public:
    explicit myServer(QObject *parent = 0);
    void startServer();
    QString get_ip_address();
signals:

public slots:

protected:
    void incomingConnection(qintptr socketDescriptor);
private:

};

#endif // MYSERVER_H
