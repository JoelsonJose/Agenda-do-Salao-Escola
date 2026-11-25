import React from 'react'
import { createRoot } from 'react-dom/client'
import AgendaPage from './features/agenda/AgendaPage.jsx'
import './styles.css'

createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <AgendaPage />
  </React.StrictMode>,
)
