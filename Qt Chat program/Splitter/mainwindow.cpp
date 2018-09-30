#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <QtWidgets>

MainWindow::MainWindow(QWidget *parent) : QMainWindow(parent), ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    createActions();
    createMenus();

    QString message = tr("Simple Qt chat program");
    statusBar()->showMessage(message);

    setWindowTitle(tr("UTPB Chat"));
    setMinimumSize(160, 160);

    ui->txt_client->installEventFilter(this);

    client_socket = NULL;
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::about()    {   QMessageBox::about(this, tr("About Menu"), tr("Craig was here."));  }

void MainWindow::createActions()
{
    ServerAct = new QAction(tr("&Server"), this);
    ServerAct->setStatusTip(tr("Connect to Server"));
    connect(ServerAct, SIGNAL(triggered()), this, SLOT(Server()));

    aboutAct = new QAction(tr("&About"), this);
    aboutAct->setStatusTip(tr("Show the application's About box"));
    connect(aboutAct, SIGNAL(triggered()), this, SLOT(about()));
}

void MainWindow::createMenus()
{
    fileMenu = menuBar()->addMenu(tr("&Server"));
    fileMenu->addAction(ServerAct);

    helpMenu = menuBar()->addMenu(tr("&Help"));
    helpMenu->addAction(aboutAct);
}

void MainWindow::Server()
{
    server_dialog = new dia_server(this);

    if (server_dialog->exec() == QDialog::Accepted)
    {
        server_address = server_dialog->get_server_address();
        port_number = server_dialog->get_port_number();

        username = server_dialog->get_username();

        if (client_socket == NULL)
            client_socket = new QTcpSocket(this);

        client_socket->connectToHost(server_address, quint16(port_number.toUShort()));

        connect(client_socket, SIGNAL(readyRead()), this, SLOT(readyRead()), Qt::DirectConnection);

        connect(client_socket, SIGNAL(connected()), this, SLOT(connected()));
        connect(client_socket, SIGNAL(error(QAbstractSocket::SocketError)),
                this, SLOT(displayError(QAbstractSocket::SocketError)));
    }
}

void MainWindow::connected()
{
    QString message = "Connected to: " + server_address + ":" + port_number;
    statusBar()->showMessage(message);

    QByteArray Data = username.toLatin1();
    Data.insert(0, '\2');
    client_socket->write(Data);

    ui->lw_username->addItem(username);
}

bool MainWindow::ok_to_add_username(QString name_to_add)
{
    for (int i = 0; i < ui->lw_username->count(); i++)
         if (ui->lw_username->item(i)->text() == name_to_add)
            return false;

    return true;
}

void MainWindow::readyRead()
{
    QByteArray Data = client_socket->readAll(), name_to_add;

    if (Data[0] == '\2')        // 2 is 'start of text' escape character, we'll use to signal a new user
    {
        int pos = 0, len = -1;
        do
        {
            len = Data.indexOf('\2', pos+1);

            if (len > 0)
            {
                name_to_add = Data.mid(pos+1, len-1);
                Data.remove(pos, len-1);
            }
            else
            {
                name_to_add = Data.right(Data.size()-1);
                Data.remove(0,1);
            }

            if (ok_to_add_username(name_to_add))
                ui->lw_username->addItem(name_to_add);


        } while (Data.indexOf('\2', pos) != -1);
    }
    else if (Data[0] == '\3')   // 3 is the end of text escape character, we'll use it to signal user disconnected
    {
        Data.remove(0,1);       // strip off the escape character

        for (int i = 0; i < ui->lw_username->count(); i++)   //look for the user name
        {
            QString itemData = ui->lw_username->item(i)->text();
            if (itemData == Data)
            {
                // if the user name is found, delete the item and return
                ui->lw_username->takeItem(i);
                return;
            }
        }
    }
    else
    {
        QString temp(Data);
        ui->txt_total_convo->appendPlainText(temp);
    }
}

bool MainWindow::send_message()
{
    bool return_value = true;
    QByteArray Data = ui->txt_client->toPlainText().toLatin1();

    if ((client_socket == NULL) ||(client_socket->write(Data) == -1))
    {
        ui->txt_total_convo->appendPlainText("Server unavailable.");
        return_value = false;
    }

    ui->txt_client->clear();

    return return_value;
}

bool MainWindow::eventFilter(QObject *obj, QEvent *event)
{
    if (event->type() == QEvent::KeyPress)
    {
        QKeyEvent *keyEvent = static_cast<QKeyEvent*>(event);
        switch(keyEvent->key())
        {
            case Qt::Key_Enter:
            case Qt::Key_Return:    send_message();
                                    return true;
        }
        return false;
    }

    return QObject::eventFilter(obj, event);
}

void MainWindow::displayError(QAbstractSocket::SocketError socketError)
{
     switch (socketError)
     {
         case QAbstractSocket::RemoteHostClosedError:   break;
         case QAbstractSocket::HostNotFoundError:       QMessageBox::information(this, tr("Client"),
                                                          tr("The host was not found. Please check the "
                                                             "host name and port settings."));
                                                         break;
         case QAbstractSocket::ConnectionRefusedError:  QMessageBox::information(this, tr("Client"),
                                                          tr("The connection was refused by the peer. "
                                                             "Make sure the server is running, "
                                                             "and check that the host name and port "
                                                             "settings are correct."));
                                                         break;
         default:                                       QMessageBox::information(this, tr("Client"),
                                                          tr("The following error occurred: %1.")
                                                          .arg(client_socket->errorString()));
     }
}

