package cresterida;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import jakarta.inject.Named;

@Named("another")
public class AnotherLambda  implements RequestHandler<InputObject, OutputObject>
{
    @Override
    public OutputObject handleRequest(InputObject input, com.amazonaws.services.lambda.runtime.Context context) {
        return new OutputObject().setResult("Another lambda");
    }
}
