import React from 'react'
import './Popup.css'

function Popup(props) {
    return (props.trigger) ? (
        
        <div className="popup">
            <div className="container popup-inner"> 
                <button className="button close-btn btn" onClick={() => props.setTrigger(false)}>x</button>
                { props.children }
                <h1 id="popup-title" class="text-center mt-2">title text</h1>
                {/* form implementation */}
                    <div className="mt-2">
                        Username:<br>{/* text input implementation*/}</br>
                    </div>
                    <div className="mt-1">
                        Password<br>{/* text input implementation*/}</br>
                    </div>
                    <label className="login">
                        {/* checkbox implementation*/} Remember me 
                    </label>
                    <div className="row">
                        <div className="col-6" id="popup-login-buttons-left">
                            <button className="button btn-max-width btn">create account</button>  
                        </div>
                        <div className="col-6" id="popup-login-buttons-right">
                            <button className="button btn-max-width btn">Login</button>  
                        </div>
                    </div>
                {/* Form implementation end */}
            </div>
        </div>
    ) : null;
}
export default Popup

{/* <div className="popup-inner">
    <button className="close-btn" onClick={() => props.setTrigger(false)}>close</button>
    { props.children }
</div> */}