import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

import './App.css';
import LoginPopup from './components/LoginPopup';
// import { useState } from 'react';

function App() {
  //const [buttonPopup, setButtonPopup] = useState(false);
  return (
    <div className="App">
      <main>
        <h1>React Popups</h1>
        <br/><br/>
        <LoginPopup/>
      </main>
    </div>
  );
}
export default App;
