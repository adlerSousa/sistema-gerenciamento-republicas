import React from 'react'
import ReactDOM from 'react-dom/client'
import { App } from './App'
import './estilos.css'

const raiz = document.getElementById('root')

if (!raiz) {
  throw new Error('O elemento raiz da aplicacao nao foi encontrado.')
}

ReactDOM.createRoot(raiz).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
