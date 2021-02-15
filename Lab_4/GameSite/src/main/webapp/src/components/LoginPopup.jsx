import React, {useState}  from 'react'
import './Popup.css'
import '../App.css'
import LoginForm from './LoginForm';
import {Button,Modal} from 'react-bootstrap';

function LoginPopupModal() {
    const [show, setShow] = useState(false);
   
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        
    <div>
      <Button variant="primary" onClick={handleShow}>
        Login
      </Button>
      <Modal size="lg" show={show} onHide={handleClose}>
        <Modal.Header closeButton>
            <Modal.Title>Title</Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <LoginForm/>
        </Modal.Body>
      </Modal>
    </div>
    );
}
export default LoginPopupModal