import { StrictMode } from "react";
import { Component } from "react";
import { ChallengeEditor } from "./app/pages/ChallengeEditor";

// using this component style to provide error handling context
export class App extends Component {
  render() {
    return (
      <StrictMode>
        <ChallengeEditor />
      </StrictMode>
    );
  }
}
