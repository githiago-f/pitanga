import { StrictMode } from "react";
import { Component } from "react";
import { ChallengeEditor } from "./pages/ChallengeEditor";

export class App extends Component {
  render() {
    return (
      <StrictMode>
        <ChallengeEditor />
      </StrictMode>
    );
  }
}
