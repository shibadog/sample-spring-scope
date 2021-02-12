package jp.github.shibadog.samplespringscope;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
@Slf4j
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@RestController
	public class DemoController {
		private final RequestContext rctx;
		private final SingletonContext sctx;
		private final PrototypeContext pctx;
		private final UUID uuid;
		private final AsyncService async;

		public DemoController(RequestContext rctx, SingletonContext sctx, PrototypeContext pctx,
				AsyncService async) {
			this.rctx = rctx;
			this.sctx = sctx;
			this.pctx = pctx;
			this.uuid = UUID.randomUUID();
			this.async = async;
		}

		@RequestMapping(value = "/test", method = RequestMethod.GET)
		public String requestMethodName() throws InterruptedException, ExecutionException {
			TimeUnit.SECONDS.sleep(1L);
			final String msg = String.format("Controller: %s\n"
					+ "PCTX: %s\n"
					+ "RCTX: %s\n"
					+ "SCTX: %s\n"
					+ "SCTX.RCTX: %s\n"
					+ "SCTX.PCTX: %s\n"
					+ "RCTX.PCTX: %s\n"
					+ "Async.RCTX: %s",
					this.uuid.toString(),
					pctx.getData(),
					rctx.getData(),
					sctx.getData(),
					sctx.getRctx().getData(),
					sctx.getPctx().getData(),
					rctx.getPctx().getData(),
					async.run().get());
			log.info(msg);
			return String.format(msg);
		}
		
	}

	@Component
	@Scope("prototype")
	public class PrototypeContext {
		private final UUID uuid;
		public PrototypeContext() {
			uuid = UUID.randomUUID();
		}

		public String getData() {
			return uuid.toString();
		}
	}

	@Component
	@RequestScope
	public class RequestContext {
		private final PrototypeContext pctx;
		private final UUID uuid;
		public RequestContext(PrototypeContext pctx) {
			this.pctx = pctx;
			uuid = UUID.randomUUID();
		}

		public String getData() {
			return uuid.toString();
		}

		public PrototypeContext getPctx() {
			return pctx;
		}
	}
	
	@Component
	public class SingletonContext {
		private final UUID uuid;
		private final RequestContext rctx;
		private final PrototypeContext pctx;
		public SingletonContext(RequestContext rctx, PrototypeContext pctx) {
			this.rctx = rctx;
			this.pctx = pctx;
			uuid = UUID.randomUUID();
		}

		public String getData() {
			return uuid.toString();
		}
	
		public RequestContext getRctx() {
			return rctx;
		}

		public PrototypeContext getPctx() {
			return pctx;
		}
	}

	@Service
	public class AsyncService {
		private final RequestContext rctx;
		public AsyncService(RequestContext rctx) {
			this.rctx = rctx;
		}

		@Async
		public CompletableFuture<String> run() {
			return CompletableFuture.completedFuture(rctx.getData());
		}
	}
}
