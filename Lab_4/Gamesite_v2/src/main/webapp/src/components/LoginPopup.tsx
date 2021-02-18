import React, {useState}  from 'react'
import {Button,Modal} from 'react-bootstrap';
import './Popup.css'
import LoginForm from './LoginForm';
import CreateAccountForm from './CreateaAccountForm';

function LoginPopupModal() {
    const [show, setShow] = useState(false);
    const [createAccount,setCreateAccount] = useState(false);
   
    const handleClose = () => {
      setShow(false);
      setCreateAccount(false);
    }
    const handleShow = () => setShow(true);
    const handleCreateAccount = () =>  setCreateAccount(true);

    return (
        
    <div>
      <Button variant="primary" onClick={handleShow}>
        Login
      </Button>
      <Modal size="lg" show={show} onHide={handleClose} animation={false}>
        <Modal.Header closeButton>
            <Modal.Title>Title</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {createAccount ? (
            <CreateAccountForm />
          ) : (
            <LoginForm handleCreateAccount={handleCreateAccount}/>
          )}
        </Modal.Body>
      </Modal>
    </div>
    );
}
export default LoginPopupModal