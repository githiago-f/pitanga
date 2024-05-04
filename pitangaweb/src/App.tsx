import { StrictMode } from 'react';
import { Component } from 'react';
import { ChallengesList } from './app/pages/ChallengesList';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import { ChallengeEditor } from './app/pages/ChallengeEditor';
import { getChallengeSolution, listChallenges } from './infra/data/pitanga.rest';
import { ErrorPage } from './app/pages/ErrorPage';
import { CreateChallenge } from './app/pages/CreateChallenge';
import { loginAction } from './app/actions/login-action';

const basename = import.meta.env.BASE_URL ?? '/pitanga-tcc';

// using this component style to provide error handling context
export class App extends Component {
  router = createBrowserRouter([
    {
      path: '/',
      element: <ChallengesList />,
      loader: listChallenges,
      errorElement: <ErrorPage />
    },
    {
      path: '/challenge/:challengeId',
      element: <ChallengeEditor />,
      loader: getChallengeSolution,
      errorElement: <ErrorPage />,
    },
    {
      path: '/create-challenge',
      element: <CreateChallenge />,
    }
  ], { basename });

  componentDidMount(): void {
    loginAction();
  }

  render() {
    return (
      <StrictMode>
        <RouterProvider router={this.router} />
      </StrictMode>
    );
  }
}
