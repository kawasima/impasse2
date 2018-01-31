import React from 'react'
import { Router, Route, Switch, Redirect } from 'react-router-dom'
import { history } from '../store'

export default function App (
  _props
) {
  return (
    <Router history={history}>
    </Router>
  )
}
